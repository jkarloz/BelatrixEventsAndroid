package com.belatrixsf.events.domain.interactors;

import com.belatrixsf.events.domain.executor.Executor;
import com.belatrixsf.events.domain.executor.MainThread;
import com.belatrixsf.events.domain.repository.Repository;

/**
 * This is an interactor boilerplate with a reference to a model repository.
 * <p/>
 */
public class SampleInteractor {

    private Callback mCallback;
    private Repository                mRepository;

    public interface Callback {
    }

    public SampleInteractor(Executor threadExecutor,
                            MainThread mainThread,
                            Callback callback, Repository repository) {
        mCallback = callback;
        mRepository = repository;
    }

   // @Override
    public void run() {
        // TODO: Implement this with your business logic
    }
}
