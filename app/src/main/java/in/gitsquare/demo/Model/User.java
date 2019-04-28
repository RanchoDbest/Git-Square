package in.gitsquare.demo.Model;

public class User {
    public User(String userImage, String userName, String userRepo, String userContribution) {
        this.userImage = userImage;
        this.userName = userName;
        this.userRepo = userRepo;
        this.userContribution = userContribution;
    }

    public String userImage,userName,userRepo,userContribution;


    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRepo() {
        return userRepo;
    }

    public void setUserRepo(String userRepo) {
        this.userRepo = userRepo;
    }

    public String getUserContribution() {
        return userContribution;
    }

    public void setUserContribution(String userContribution) {
        this.userContribution = userContribution;
    }
}
