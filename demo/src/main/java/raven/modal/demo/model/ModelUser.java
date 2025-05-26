package raven.modal.demo.model;

public class ModelUser {

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ModelUser(String userName, String mail, Role role) {
        this.userName = userName;
        this.mail = mail;
        this.role = role;
    }

    private String userName;
    private String mail;
    private Role role;

    public enum Role {
        ADMIN, STAFF;

        @Override
        public String toString() {
            if (this == ADMIN) {
                return "Admin";
            }
            return "Staff";
        }
    }
}
