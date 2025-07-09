package org.shop.services.admin;

public interface IAdminService {
    void BanUser(String userId);
    void UnBanUser(String userId);
    void updateUser(String id, String status);
    void ChangeOrderStatus(String id, String status);

}
