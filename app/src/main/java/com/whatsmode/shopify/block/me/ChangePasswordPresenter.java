package com.whatsmode.shopify.block.me;

import android.text.TextUtils;

import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.AccountRepository;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.account.data.UserInfo;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by tom on 17-11-30.
 */

public class ChangePasswordPresenter extends BaseRxPresenter<ChangePasswordContract.View> implements ChangePasswordContract.Presenter {

    @Override
    public void setPassword(String currentPwd, String newPwd) {
        final String username = AccountManager.getUsername();
        if (TextUtils.isEmpty(username)) {
            return;
        }
        Storefront.CustomerAccessTokenCreateInput input = new Storefront.CustomerAccessTokenCreateInput(username,currentPwd);

        AccountRepository.create().login(input, new LoginFragment())  //1.验证旧密码
                .flatMap(f -> {
                    //2.设置新密码
                    Storefront.CustomerUpdateInput inputT = new Storefront.CustomerUpdateInput();
                    if (!TextUtils.isEmpty(newPwd)) inputT.setPassword(newPwd);
                    return MyRepository.create().updateCustomer(AccountManager.getCustomerAccessToken(),inputT,new UpdateCustomerFragment());
                })
                .flatMap(f -> {
                    //3.重新登录
                    Storefront.CustomerAccessTokenCreateInput inputN = new Storefront.CustomerAccessTokenCreateInput(username,newPwd);
                    Single<Storefront.CustomerAccessToken> login = AccountRepository.create().login(inputN, new LoginFragment());

                    return login;
                }).map(t -> {
                    AccountManager.getInstance().writeCustomerAccessToken(t != null ? t.getAccessToken() : null);
                    AccountManager.getInstance().writeCustomerUserInfo(new UserInfo(username));
                    return t;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Storefront.CustomerAccessToken>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Storefront.CustomerAccessToken token) {
                        if (isViewAttached()) {
                            getView().updateSuccess();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (isViewAttached()) {
                            String message = e.getMessage();
                            if ("Unidentified customer".equals(message)) {
                                message = WhatsApplication.getContext().getString(R.string.current_password_input_is_mistaken);
                            }
                            if (e instanceof APIException) {
                                APIException t = (APIException) e;
                                getView().onError(t.getCode(),message);
                            }else{
                                getView().onError(APIException.CODE_COMMON_EXCEPTION,message);
                            }
                        }
                    }
                });

    }

    @Override
    public void setName(String firstName, String lastName) {
        Storefront.CustomerUpdateInput input = new Storefront.CustomerUpdateInput();
        if (!TextUtils.isEmpty(firstName)) input.setFirstName(firstName);
        if (!TextUtils.isEmpty(lastName)) input.setLastName(lastName);
        MyRepository.create().updateCustomer(AccountManager.getCustomerAccessToken(),input,new UpdateCustomerFragment())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Storefront.Customer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Storefront.Customer customer) {
                        if (isViewAttached()) {
                            getView().updateSuccess();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (isViewAttached()) {
                            if (e instanceof APIException) {
                                APIException t = (APIException) e;
                                getView().onError(t.getCode(),t.getMessage());
                            }else{
                                getView().onError(APIException.CODE_COMMON_EXCEPTION,e.getMessage());
                            }
                        }
                    }
                });
    }

    //update password
    public class UpdateCustomerFragment implements Storefront.CustomerUpdatePayloadQueryDefinition{

        @Override
        public void define(Storefront.CustomerUpdatePayloadQuery _queryBuilder) {
            _queryBuilder.userErrors(e -> e.message().field()).customer(c -> c.id().email().firstName().lastName());
        }
    }

    //login
    public class LoginFragment implements Storefront.CustomerAccessTokenCreatePayloadQueryDefinition{

        @Override
        public void define(Storefront.CustomerAccessTokenCreatePayloadQuery _queryBuilder) {
            _queryBuilder.customerAccessToken(
                    _queryBuilder1 -> _queryBuilder1.accessToken().expiresAt())
                    .userErrors(_queryBuilder2 -> _queryBuilder2.message().field());
        }
    }
}
