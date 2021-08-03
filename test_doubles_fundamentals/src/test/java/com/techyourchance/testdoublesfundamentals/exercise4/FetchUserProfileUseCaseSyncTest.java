package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.UseCaseResult;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FetchUserProfileUseCaseSyncTest {

    FetchUserProfileUseCaseSync SUT;
    UserProfileEndpointSyncTest mUserProfileHttpEndpointSync;
    UsersCache mUsersCache;
    public static final String USERID = "Userid";
    public static final String FULL_NAME = "Fullname";
    public static final String IMAGE_URL = "ImageUrl";

    @Before
    public void setup() {
        mUserProfileHttpEndpointSync = new UserProfileEndpointSyncTest();
        mUsersCache = new UsersCacheTest();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSync, mUsersCache);
    }

    //getUserProfile success passed id
    @Test
    public void getUserProfile_success_PassedIdToEndpoint() throws Exception {
        SUT.fetchUserProfileSync(USERID);
        System.out.println(mUserProfileHttpEndpointSync.mUserId);
        assertThat(mUserProfileHttpEndpointSync.mUserId, is(USERID));
        assertThat(mUsersCache.getUser(USERID), is(notNullValue()));
    }

    //getUserProfile failed general error
    @Test
    public void getUserProfile_Success_UserCached() throws Exception {
        SUT.fetchUserProfileSync(USERID);
        User usersCache = mUsersCache.getUser(USERID);
        assertThat(usersCache.getUserId(), is(USERID));
        assertThat(usersCache.getFullName(), is(FULL_NAME));
        assertThat(usersCache.getImageUrl(), is(IMAGE_URL));
    }

    @Test
    public void getUserProfile_success_EndpointSuccess() throws Exception {
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void getUserProfile_success_generalError() throws Exception {
        mUserProfileHttpEndpointSync.isGeneralError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    //getUserProfile failed auth error
    @Test
    public void getUserProfile_success_AuthError() throws Exception {
        mUserProfileHttpEndpointSync.isAuthError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    //getUserProfile failed server error
    @Test
    public void getUserProfile_success_ServerError() throws Exception {
        mUserProfileHttpEndpointSync.isServerError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    //getUserProfile failed network error
    @Test
    public void getUserProfile_success_NetworkError() throws Exception {
        mUserProfileHttpEndpointSync.isNetworkError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    //getUserProfile_Success_UserCached

    @Test
    public void getUserProfile_AUTHFailure_NotUserCached() throws Exception {
        mUserProfileHttpEndpointSync.isAuthError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        User usersCache = mUsersCache.getUser(USERID);
        assertThat(usersCache, is(nullValue()));

    }

    @Test
    public void getUserProfile_failure_EndpointAuthError() throws Exception {
        mUserProfileHttpEndpointSync.isAuthError=true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }
    @Test
    public void getUserProfile_failure_EndpointGenralError() throws Exception {
        mUserProfileHttpEndpointSync.isGeneralError=true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }
    @Test
    public void getUserProfile_failure_EndpointServerError() throws Exception {
        mUserProfileHttpEndpointSync.isServerError=true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }
    @Test
    public void getUserProfile_failure_EndpointNetworkError() throws Exception {
        mUserProfileHttpEndpointSync.isNetworkError=true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }
    @Test
    public void getUserProfile_generalError_UserNotCached() throws Exception{
        mUserProfileHttpEndpointSync.isGeneralError=true;
        SUT.fetchUserProfileSync(USERID);
        assertThat(mUsersCache.getUser(USERID),is(nullValue()));
    }

    @Test
    public void getUserProfile_serverError_UserNotCached() throws Exception{
        mUserProfileHttpEndpointSync.isServerError=true;
        SUT.fetchUserProfileSync(USERID);
        assertThat(mUsersCache.getUser(USERID),is(nullValue()));
    } @Test
    public void getUserProfile_authError_UserNotCached() throws Exception{
        mUserProfileHttpEndpointSync.isAuthError=true;
        SUT.fetchUserProfileSync(USERID);
        assertThat(mUsersCache.getUser(USERID),is(nullValue()));
    }

    //--------------Helper classes-------------------

    private static class UserProfileEndpointSyncTest implements UserProfileHttpEndpointSync {

        private boolean isGeneralError;
        private boolean isServerError;
        private boolean isNetworkError;
        private boolean isAuthError;
        private String mUserId = "";

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;
            if (isGeneralError) {
                return new EndpointResult(EndpointResultStatus.FAILURE, "", "", "");
            } else if (isNetworkError) {
                throw new NetworkErrorException();
            } else if (isServerError) {
                return new EndpointResult(EndpointResultStatus.FAILURE, "", "", "");

            } else if (isAuthError) {
                return new EndpointResult(EndpointResultStatus.FAILURE, "", "", "");
            }

            return new EndpointResult(EndpointResultStatus.SUCCESS, USERID, FULL_NAME, IMAGE_URL);
        }
    }

    private static class UsersCacheTest implements UsersCache {
        private List<User> mUsers = new ArrayList<>(1);

        @Override
        public void cacheUser(User user) {
            System.out.println("cache user: " + user.getUserId());
            User existing_user = getUser(user.getUserId());
            if (existing_user != null) {
                mUsers.remove(existing_user);
            }

            mUsers.add(user);
            System.out.println("cache user: mUsers added " + mUsers.size());

        }

        @Nullable
        @Override
        public User getUser(String userId) {
            System.out.println("getuser: " + userId + "Size: " + mUsers.size());


            for (User user : mUsers) {
                System.out.println("For loop" + (user.getUserId().equals(userId)));
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }
    }

}