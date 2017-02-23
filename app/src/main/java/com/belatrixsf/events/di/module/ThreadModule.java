package com.belatrixsf.events.di.module;


import com.belatrixsf.events.di.scope.ApplicationScope;
import com.belatrixsf.events.domain.executor.Executor;
import com.belatrixsf.events.domain.executor.MainThread;
import com.belatrixsf.events.domain.executor.impl.MainThreadImpl;
import com.belatrixsf.events.domain.executor.impl.ThreadExecutor;

import dagger.Module;
import dagger.Provides;

@Module
public class ThreadModule {


    @Provides
    public MainThread getMainThread() {
        return new MainThreadImpl();
    }

    @Provides
    public Executor getExecutor() {
        return new ThreadExecutor();
    }


}