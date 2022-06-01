package woowacourse.member.dto.request;

import javax.validation.constraints.NotBlank;

public class UpdatePasswordRequest {

    @NotBlank(message = "비밀번호는 빈 값일 수 없습니다.")
    private String oldPassword;
    @NotBlank(message = "비밀번호는 빈 값일 수 없습니다.")
    private String newPassword;

    public UpdatePasswordRequest() {

    }

    public UpdatePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
