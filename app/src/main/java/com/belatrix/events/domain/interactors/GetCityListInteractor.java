package com.belatrix.events.domain.interactors;

import com.belatrix.events.data.datasource.ServerCallback;
import com.belatrix.events.domain.executor.Executor;
import com.belatrix.events.domain.executor.MainThread;
import com.belatrix.events.domain.interactors.base.AbstractInteractor;
import com.belatrix.events.domain.model.City;
import com.belatrix.events.domain.repository.EventRepository;

import java.util.List;

import javax.inject.Inject;


public class GetCityListInteractor extends AbstractInteractor<GetCityListInteractor.CallBack,Void> {

    public interface CallBack {
        void onSuccess(List<City> result);
        void onError();
    }

    @Inject
    EventRepository eventRepository;

    @Inject
    public GetCityListInteractor(Executor mThreadExecutor, MainThread mMainThread) {
        super(mThreadExecutor, mMainThread);
    }


    @Override
    public void run(Void ...params) {
        eventRepository.cityList(serverCallback);
    }

    ServerCallback serverCallback = new ServerCallback<List<City>>() {
        @Override
        public void onSuccess(final List<City> response) {
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(response);
                }
            });
        }

        @Override
        public void onFail(int statusCode, final String errorMessage) {
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    callback.onError();
                }
            });
        }

        @Override
        public void onError(String errorMessage) {
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    callback.onError();
                }
            });
        }
    };

    @Override
    public void onError(Exception e) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                callback.onError();
            }
        });
    }

}