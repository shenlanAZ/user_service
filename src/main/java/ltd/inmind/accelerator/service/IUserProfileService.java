package ltd.inmind.accelerator.service;

import ltd.inmind.accelerator.model.po.UserProfile;

public interface IUserProfileService {

    UserProfile getByUId(Long uId);

    void register(UserProfile userProfile);

    void updateProfile(UserProfile userProfile);
}
