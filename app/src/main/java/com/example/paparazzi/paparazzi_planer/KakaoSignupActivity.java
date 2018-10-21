package com.example.paparazzi.paparazzi_planer;

/**
 * Created by LeeJinKyu on 2018-04-19.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

public class KakaoSignupActivity extends Activity{
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    public static final String NICKNAME = "nick";
    public static final String USER_ID = "id";
    public static final String PROFILE_IMG = "img";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Logger.d("UserProfile : " + userProfile);

                Log.d("test","로그인 성공!");
//로그인 성공 시 로그인한 사용자의 일련번호, 닉네임, 이미지url 리턴
                //사용자 캐시 정보 업데이트 - 별 필요 없는듯
                if (userProfile != null) {
                    userProfile.saveUserToCache();
                }
                Logger.e("succeeded to update user profile",userProfile,"\n");
                //////////////////

                final String nickName = userProfile.getNickname();//닉네임
                final long userID = userProfile.getId();//사용자 고유번호
                final String pImage = userProfile.getProfileImagePath();//사용자 프로필 경로
                Log.e("UserProfile", userProfile.toString());//전체 정보 출력

                Intent intent = new Intent(KakaoSignupActivity.this, MainActivity.class);
                intent.putExtra(NICKNAME,nickName);
                intent.putExtra(USER_ID,String.valueOf(userID));
                intent.putExtra(PROFILE_IMG,pImage);
                startActivity(intent);
                finish();

            }
        });
    }

  /*  private void redirectMainActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }*/
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(KakaoSignupActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
