package com.github.polydome.journow.di;

import com.github.polydome.journow.data.event.DataEvent;
import com.github.polydome.journow.data.event.DataEventBus;
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

    @Provides
    @Named("TaskDataEvents")
    Observable<DataEvent> taskDataEvents(DataEventBus dataEventBus) {
        return dataEventBus.taskEvents();
    }
}
