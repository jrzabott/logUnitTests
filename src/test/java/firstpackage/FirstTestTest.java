package firstpackage;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.devexperts.logging.Logging;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FirstTestTest {

    private FirstTest firstTest;

    @BeforeEach
    void setUp() {
        this.firstTest = new FirstTest();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void returnLetterA() {
        assertThat(firstTest.returnLetterA()).isEqualTo("A");
    }

    @Test
    void logAMessageUsingSLF4JLogger() {
        Logger log = (Logger) LoggerFactory.getLogger(FirstTest.class.getName());
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);

        firstTest.produceAInfoLog();
        final List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList).hasSize(1);

        logList.forEach(System.out::println);

    }

    @Test
    void logAMessageUsingDevexpertsLogging() {;
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        MyAppender appender = new MyAppender();
        config.getRootLogger().addAppender(appender, Level.ALL, ThresholdFilter.createFilter(Level.ALL, Filter.Result.ACCEPT, Filter.Result.DENY));

        firstTest.produceAInfoLog();

        final List<LogEvent> logEvents = appender.logEvents;
        final List<String> logMessages = appender.logMessages;
        assertThat(logEvents).hasSize(1);
        assertThat(logMessages).hasSize(1);
        logEvents.forEach(e -> System.out.println(Objects.toString(e.getMessage().getFormattedMessage(), "No Content")));
        logMessages.forEach(System.out::println);

    }

    class MyAppender implements Appender {
        List<LogEvent> logEvents = new ArrayList<>();
        List<String> logMessages = new ArrayList<>();

        @Override
        public void append(LogEvent event) {
            logEvents.add(event);
            logMessages.add(event.getMessage().getFormattedMessage());
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Layout<? extends Serializable> getLayout() {
            return null;
        }

        @Override
        public boolean ignoreExceptions() {
            return true;
        }

        @Override
        public ErrorHandler getHandler() {
            return null;
        }

        @Override
        public void setHandler(ErrorHandler handler) {

        }

        @Override
        public State getState() {
            return null;
        }

        @Override
        public void initialize() {

        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }

        @Override
        public boolean isStarted() {
            return true;
        }

        @Override
        public boolean isStopped() {
            return false;
        }
    }


    @Test
    void logAMessageUsingDevexpertsLoggingWithMockAppender(){

        List<String> logMessages = new ArrayList<>();
        Appender appender = mock(Appender.class);
        when(appender.ignoreExceptions()).thenReturn(true);
        final ErrorHandler errorHandler = mock(ErrorHandler.class);
        when(appender.getHandler()).thenReturn(errorHandler);
        doAnswer(invocation -> {
            final Object argument = invocation.getArgument(0);
            if (argument instanceof LogEvent) {
                logMessages.add(((LogEvent) argument).getMessage().getFormattedMessage());
            }
            return null;
        }).when(appender).append(any());

        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        config.getRootLogger().addAppender(appender, Level.ALL, mock(Filter.class));

        firstTest.produceAInfoLog();
        assertThat(logMessages).hasSize(1);
        logMessages.forEach(System.out::println);
    }

}