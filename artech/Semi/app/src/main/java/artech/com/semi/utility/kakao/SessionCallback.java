package artech.com.semi.utility.kakao;

import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

/**
 * Created by moon on 2018-04-27.
 */

public class SessionCallback implements ISessionCallback {

    @Override
    public void onSessionOpened() {

        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    //에러로 인한 로그인 실패
                    Log.e("KAKAO", "onFailure message: " + message);
//                        finish();
                } else {
                    Log.e("KAKAO", "onFailure result: " + result);
                    //redirectMainActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("KAKAO", "onSessionClosed result: " + errorResult.getErrorMessage());
            }

            @Override
            public void onNotSignedUp() {
                Log.e("KAKAO", "onNotSignedUp" );
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.

                    Log.e("KAKAO", "UserProfile userProfile : " + userProfile.toString());
                    Log.e("KAKAO", "UserProfile getNickname: " + userProfile.getNickname());
                    Log.e("KAKAO", "UserProfile getId  : " + userProfile.getId());
                    Log.e("KAKAO", "UserProfile getUUID: " + userProfile.getUUID());
                    Log.e("KAKAO", "UserProfile getServiceUserId: " + userProfile.getServiceUserId());


                long number = userProfile.getId();


            }
        });

    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        // 세션 연결이 실패했을때
        // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ
        Log.e("KAKAO", "onSessionOpenFailed: " + exception.getMessage());

    }
}