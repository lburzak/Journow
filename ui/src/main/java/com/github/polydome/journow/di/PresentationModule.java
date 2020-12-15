package com.github.polydome.journow.di;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Named;
import java.util.concurrent.TimeUnit;

@Module(includes = {DomainModule.class})
public class PresentationModule {
    @Provides
    @Named("TimerUpdateInterval")
    Observable<Long> timerUpdateInterval() {
        return Observable.interval(100, TimeUnit.MILLISECONDS);
    }
}
