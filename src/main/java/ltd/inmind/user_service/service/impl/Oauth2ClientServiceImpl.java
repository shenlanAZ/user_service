package ltd.inmind.user_service.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ltd.inmind.user_service.mapper.Oauth2ClientMapper;
import ltd.inmind.user_service.model.Oauth2Client;
import ltd.inmind.user_service.service.Oauth2ClientService;
import ltd.inmind.user_service.utils.UUIDUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Oauth2ClientServiceImpl implements Oauth2ClientService {

    @Autowired
    private Oauth2ClientMapper oauth2ClientMapper;

    private Map<String, Long> CODE_MAP = new ConcurrentHashMap<>();

    private long EXPIRED_TIME = 1000 * 60 * 10; //10 min



    @Override
    public boolean newClient(Oauth2Client oauth2Client) {
        try {

            //生成clientID clientSecret
            String clientId = UUIDUtil.generateShortUuid();
            String secret = clientId + "_" + oauth2Client.getCallbackUrl() + "_" + System.currentTimeMillis();
            String clientSecret = DigestUtils.sha256Hex(secret);

            oauth2Client.setClientId(clientId);
            oauth2Client.setClientSecret(clientSecret);

            oauth2ClientMapper.insert(oauth2Client);
        } catch (Exception e) {

            //LOG
            return false;
        }

        return true;
    }

    @Override
    public boolean verifyClientId(String clientId) {
        try {

            Oauth2Client oauth2Client = oauth2ClientMapper.selectById(clientId);
            return oauth2Client != null;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String grantCode() {
        try {
            String code = UUIDUtil.generateShortUuid();
            CODE_MAP.put(code, System.currentTimeMillis());
            return code;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String accessToken(String client_id, String client_secret, String code) {

        Oauth2Client oauth2Client = oauth2ClientMapper.selectOne(
                Wrappers.<Oauth2Client>lambdaQuery()
                        .eq(Oauth2Client::getClientId, client_id));

        if (oauth2Client == null)
            throw new RuntimeException("client id error");

        if (!oauth2Client.getClientSecret().equals(client_secret))
            throw new RuntimeException("secret invalid");

        if (!CODE_MAP.containsKey(code))
            throw new RuntimeException("code invalid");

        if ((CODE_MAP.get(code) + EXPIRED_TIME) <= System.currentTimeMillis())
            throw new RuntimeException("code expired");

        //TODO 调整token的内容
        String token = client_id + "_" + client_secret + "_" + UUIDUtil.generateShortUuid();

        return DigestUtils.sha256Hex(token);
    }

}
