package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.UseCaseResult.FAILURE;
import static com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR;
import static com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS;
import static com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResult;
import static com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    private static final String USER_ID = "UserId";
    private static final String USERNAME = "Username";

    UpdateUsernameUseCaseSync SUT;
    @Mock UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSyncMock;
    @Mock UsersCache mUsersCacheMock;
    @Mock EventBusPoster mEventBusPosterMock;

    @Before
    public void setup() throws NetworkErrorException {
        SUT = new UpdateUsernameUseCaseSync(mUpdateUsernameHttpEndpointSyncMock, mUsersCacheMock, mEventBusPosterMock);
        success();
    }

    /*
     *   Test cases begins
     *   1. Test userid and username is passed
     *   2. Test Endpoint Result success
     *   3. Test usercache saved when success
     *   4. Test when Success LoggedInEventPosted
     *   5. Test does not cache when any Error occured
     *   6. Test does not LoggedInEventPoster when any Error occured
     *   7. Test Endpoint Result Failure when any Error occured
     *
     * */
    @Test
    public void updateUsername_success_passedUseridAndUsernameToEndpoint() throws NetworkErrorException {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(mUpdateUsernameHttpEndpointSyncMock, times(1)).updateUsername(ac.capture(), ac.capture());
        List<String> list = ac.getAllValues();
        assertThat(list.get(0), is(USER_ID));
        assertThat(list.get(1), is(USERNAME));

    }

    @Test
    public void updateUsername_Success_EndpointSuccess() {
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(SUCCESS));

    }

    @Test
    public void updateUser_Success_UserCached() {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(mUsersCacheMock).cacheUser(ac.capture());
        User cacheUser = ac.getValue();
        assertThat(cacheUser.getUserId(), is(USER_ID));
        assertThat(cacheUser.getUsername(), is(USERNAME));

    }

    @Test
    public void updateUsername_Success_eventPosted() {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertThat(ac.getValue(), is(instanceOf(UserDetailsChangedEvent.class)));
    }

    @Test
    public void updateusername_generalError_userNotCached() throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }


    @Test
    public void updateUSername_AuthError_UserNotCahced() throws NetworkErrorException {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    @Test
    public void updateUsername_serverError_userNotCached() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    @Test
    public void updateusername_authError_eventNotPosted() throws Exception {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateusername_serverError_eventNotPosted() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateusername_generalError_eventNotPosted() throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateusername_generalError_FailureReturned() throws Exception {
        generalError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(FAILURE));
    }

    @Test
    public void updateusername_authError_FailureReturned() throws Exception {
        authError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(FAILURE));
    }

    @Test
    public void updateusername_serverError_FailureReturned() throws Exception {
        serverError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(FAILURE));
    }

    @Test
    public void updateusername_networkError_NetworkReturned() throws Exception {
        networkError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(NETWORK_ERROR));
    }

    private void success() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USERNAME));
    }

    private void generalError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", ""));
    }

    private void serverError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", ""));
    }

    private void authError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", ""));
    }

    private void networkError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenThrow(new NetworkErrorException());
    }
}