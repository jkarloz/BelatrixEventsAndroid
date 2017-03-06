package com.belatrixsf.events.domain.interactors;

import com.belatrixsf.events.domain.executor.Executor;
import com.belatrixsf.events.domain.executor.MainThread;
import com.belatrixsf.events.domain.interactors.base.AbstractInteractor;
import com.belatrixsf.events.domain.model.Event;

import java.util.List;

import javax.inject.Inject;



public class EventListInteractor extends AbstractInteractor<List<Event>,Void> {


    @Inject
    public EventListInteractor(Executor mThreadExecutor, MainThread mMainThread) {
        super(mThreadExecutor, mMainThread);
    }


    @Override
    public void run(Void ...params) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onResult(Event.getDummyEventListData());
            }
        });
    }
}