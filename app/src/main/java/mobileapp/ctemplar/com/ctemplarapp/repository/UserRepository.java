package mobileapp.ctemplar.com.ctemplarapp.repository;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import mobileapp.ctemplar.com.ctemplarapp.CTemplarApp;
import mobileapp.ctemplar.com.ctemplarapp.net.RestService;
import mobileapp.ctemplar.com.ctemplarapp.net.request.AddFirebaseTokenRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.AttachmentsEncryptedRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.AutoSaveContactEnabledRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.CaptchaVerifyRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.ChangePasswordRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.CheckUsernameRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.ContactsEncryptionRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.CreateMailboxRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.CustomFilterRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.DefaultMailboxRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.EnabledMailboxRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.MarkMessageAsReadRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.MarkMessageIsStarredRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.MoveToFolderRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.PublicKeysRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.RecoverPasswordRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.RecoveryEmailRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.SendMessageRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.SignInRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.SignUpRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.SignatureRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.request.SubjectEncryptedRequest;
import mobileapp.ctemplar.com.ctemplarapp.net.response.AddFirebaseTokenResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.CaptchaResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.CaptchaVerifyResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.CheckUsernameResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Domains.DomainsResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Filters.FilterResult;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Filters.FiltersResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.KeyResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Mailboxes.MailboxesResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Mailboxes.MailboxesResult;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Messages.MessageAttachment;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Messages.MessagesResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Messages.MessagesResult;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Myself.BlackListContact;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Myself.MyselfResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Myself.SettingsEntity;
import mobileapp.ctemplar.com.ctemplarapp.net.response.Myself.WhiteListContact;
import mobileapp.ctemplar.com.ctemplarapp.net.response.RecoverPasswordResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.SignInResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.SignUpResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.WhiteBlackLists.BlackListResponse;
import mobileapp.ctemplar.com.ctemplarapp.net.response.WhiteBlackLists.WhiteListResponse;
import mobileapp.ctemplar.com.ctemplarapp.repository.entity.MailboxEntity;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

@Singleton
public class UserRepository {

    private static UserRepository instance = new UserRepository();
    private RestService service;
    private UserStore userStore;

    public static UserRepository getInstance() {
        if(instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public UserRepository() {
        service = CTemplarApp.getRestClient().getRestService();
        userStore = CTemplarApp.getUserStore();
    }

    public void clearToken() {
        userStore.clearToken();
    }

    public void saveUserToken(String token) {
        userStore.saveToken(token);
    }

    public String getUserToken() {
        return userStore.getToken();
    }

    public void saveFirebaseToken(String token) {
        userStore.saveFirebaseToken(token);
    }

    public String getFirebaseToken() {
        return userStore.getFirebaseToken();
    }

    public void saveUserPassword(String password) {
        userStore.savePassword(password);
    }

    public String getUserPassword() {
        return userStore.getUserPassword();
    }

    public void saveUsername(String username) {
        userStore.saveUsername(username);
    }

    public String getUsername() {
        return userStore.getUsername();
    }

    public void saveTimeZone(String timezone) {
        userStore.saveTimeZone(timezone);
    }

    public String getTimeZone() {
        return userStore.getTimeZone();
    }

    public void setNotificationsEnabled(boolean isEnabled) {
        userStore.setNotificationsEnabled(isEnabled);
    }

    public boolean isNotificationsEnabled() {
        return userStore.getNotificationsEnabled();
    }

    public void setAttachmentsEncryptionEnabled(boolean state) {
        userStore.setAttachmentsEncryptionEnabled(state);
    }

    public boolean getAttachmentsEncryptionEnabled() {
        return userStore.getAttachmentsEncryptionEnabled();
    }

    public void setContactsEncryptionEnabled(boolean isContactsEncryptionEnabled) {
        userStore.setContactsEncryptionEnabled(isContactsEncryptionEnabled);
    }

    public boolean getContactsEncryptionEnabled() {
        return userStore.getContactsEncryptionEnabled();
    }

    public void logout() {
        userStore.logout();
        CTemplarApp.getAppDatabase().clearAllTables();
    }

    public void saveMailboxes(List<MailboxesResult> mailboxes) {
        if(mailboxes != null && mailboxes.size()>0) {
            for(int i = 0; i < mailboxes.size(); i++) {
                MailboxesResult result = mailboxes.get(i);

                MailboxEntity entity = new MailboxEntity();
                entity.setId(result.getId());
                entity.setDefault(result.isDefault());
                entity.setDisplayName(result.getDisplayName());
                entity.setEmail(result.getEmail());
                entity.setEnabled(result.isEnabled());
                entity.setFingerprint(result.getFingerprint());
                entity.setPrivateKey(result.getPrivateKey());
                entity.setPublicKey(result.getPublicKey());
                entity.setSignature(result.getSignature());

                CTemplarApp.getAppDatabase().mailboxDao().save(entity);
            }
        }
    }

    // Requests
    public Observable<SignInResponse> signIn(SignInRequest request) {
        return service.signIn(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SignUpResponse> signUp(SignUpRequest request) {
        return service.signUp(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CheckUsernameResponse> checkUsername(CheckUsernameRequest request) {
        return service.checkUsername(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<RecoverPasswordResponse> recoverPassword(RecoverPasswordRequest request) {
        return service.recoverPassword(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseBody> changePassword(ChangePasswordRequest request) {
        return service.changePassword(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<RecoverPasswordResponse> resetPassword(RecoverPasswordRequest request) {
        return service.resetPassword(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessagesResponse> getMessagesList(int limit, int offset, String folder) {
        return service.getMessages(limit, offset, folder)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessagesResponse> getStarredMessagesList(int limit, int offset, int starred) {
        return service.getStarredMessages(limit, offset, starred)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessagesResponse> getMessage(long id) {
        return service.getMessage(id)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<Void>> deleteMessages(String messageIds) {
        return service.deleteMessages(messageIds)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<Void>> toFolder(long id, String folder) {
        return service.toFolder(id, new MoveToFolderRequest(folder))
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessagesResponse> getChainMessages(long id) {
        return service.getChainMessages(id)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<Void>> markMessageAsRead(long id, MarkMessageAsReadRequest request) {
        return service.markMessageAsRead(id, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<Void>> markMessageIsStarred(long id, boolean starred) {
        return service.markMessageIsStarred(id, new MarkMessageIsStarredRequest(starred))
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MyselfResponse> getMyselfInfo() {
        return service.getMyself()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<MailboxesResponse> getMailboxesList(int limit, int offset) {
        return service.getMailboxes(limit, offset)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessagesResult> updateMessage(long id, SendMessageRequest request) {
        return service.updateMessage(id, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessagesResult> sendMessage(SendMessageRequest request) {
        return service.sendMessage(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessageAttachment> uploadAttachment(MultipartBody.Part attachment, long message, boolean isEncrypted) {
        return service.uploadAttachment(attachment, message, isEncrypted)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessageAttachment> updateAttachment(long id, MultipartBody.Part attachment, long message, boolean isEncrypted) {
        return service.updateAttachment(id, attachment, message, isEncrypted)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<Void>> deleteAttachment(long id) {
        return service.deleteAttachment(id)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<KeyResponse> getEmailPublicKeys(PublicKeysRequest request) {
        return service.getKeys(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<FiltersResponse> getFilterList() {
        return service.getFilterList()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<FilterResult> createFilter(CustomFilterRequest customFilterRequest) {
        return service.createFilter(customFilterRequest)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<FilterResult> updateFilter(long id, CustomFilterRequest customFilterRequest) {
        return service.updateFilter(id, customFilterRequest)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<Void>> deleteFilter(long id) {
        return service.deleteFilter(id)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseBody> deleteBlacklistContact(BlackListContact contact) {
        return service.deleteBlacklistContact(contact.id)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseBody> deleteWhitelistContact(WhiteListContact contact) {
        return service.deleteWhitelistContact(contact.id)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BlackListResponse> getBlackListContacts() {
        return service.getBlackListContacts()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BlackListContact> addBlacklistContact(BlackListContact contact) {
        return service.addBlacklistContact(contact)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WhiteListResponse> getWhiteListContacts() {
        return service.getWhiteListContacts()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WhiteListContact> addWhitelistContact(WhiteListContact contact) {
        return service.addWhitelistContact(contact)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MailboxesResult> updateDefaultMailbox(long mailboxId, DefaultMailboxRequest request) {
        return service.updateDefaultMailbox(mailboxId, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MailboxesResult> updateEnabledMailbox(long mailboxId, EnabledMailboxRequest request) {
        return service.updateEnabledMailbox(mailboxId, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<MailboxesResult>> createMailbox(CreateMailboxRequest request) {
        return service.createMailbox(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DomainsResponse> getDomains() {
        return service.getDomains()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SettingsEntity> updateRecoveryEmail(long settingId, RecoveryEmailRequest request) {
        return service.updateRecoveryEmail(settingId, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SettingsEntity> updateSubjectEncrypted(long settingId, SubjectEncryptedRequest request) {
        return service.updateSubjectEncrypted(settingId, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SettingsEntity> updateAttachmentsEncrypted(long settingId, AttachmentsEncryptedRequest request) {
        return service.updateAttachmentsEncrypted(settingId, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SettingsEntity> updateContactsEncryption(long settingId, ContactsEncryptionRequest request) {
        return service.updateContactsEncryption(settingId, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SettingsEntity> updateAutoSaveEnabled(long settingId, AutoSaveContactEnabledRequest request) {
        return service.updateAutoSaveEnabled(settingId, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SettingsEntity> updateSignature(long mailboxId, SignatureRequest request) {
        return service.updateSignature(mailboxId, request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CaptchaResponse> getCaptcha() {
        return service.getCaptcha()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CaptchaVerifyResponse> captchaVerify(CaptchaVerifyRequest request) {
        return service.captchaVerify(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<AddFirebaseTokenResponse> addFirebaseToken(AddFirebaseTokenRequest request) {
        return service.addFirebaseToken(request)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<Void>> deleteFirebaseToken(String token) {
        return service.deleteFirebaseToken(token)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
