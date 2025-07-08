package org.shop.repositories.admin;

public interface IAdminRepository {
    public void DeleteUser(String UserId);
    public void BanUser(String userId);
    public void DeleteOrder(String orderId);
    public void PromoteUserToAdmin(String userId);
}
