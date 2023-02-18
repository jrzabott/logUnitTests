package firstpackage;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FirstTestTest {

    private static MyAppender appender;
    private static Configuration config;
    private final FirstTest firstTest = new FirstTest();

    @BeforeAll
    static void beforeAll() {
        appender = new MyAppender();
    }

    @BeforeEach
    void resetAppender() {
        appender.reset();
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        config = ctx.getConfiguration();
        config.getRootLogger().addAppender(appender, Level.ALL, ThresholdFilter.createFilter(Level.ALL, Filter.Result.ACCEPT, Filter.Result.DENY));
    }

    @AfterEach
    void tearDown() {
        config.getRootLogger().removeAppender(appender.getName());
    }

    @Test
    void returnLetterA() {
        assertThat(firstTest.returnLetterA()).isEqualTo("A");
    }

    @Test
    void logAMessageUsingDevexpertsLogging() {
        firstTest.produceAInfoLog();

        appender.getLogEvents().forEach(e -> System.out.println(Objects.toString(e.getMessage().getFormattedMessage(), "No Content")));
        appender.getLogMessages().forEach(System.out::println);
        assertThat(appender.getLogEvents()).hasSize(1);
        assertThat(appender.getLogMessages()).hasSize(1);

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

        // logList.forEach(System.out::println);

    }

    @Test
    void logAMessageUsingDevexpertsLoggingWithMockAppender() {

        List<String> logMessages = new ArrayList<>();
        Appender appender = mock(Appender.class);
        when(appender.getName()).thenReturn("MockAppender");
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

        config.getRootLogger().addAppender(appender, Level.ALL, mock(Filter.class));
        //config.getAppenders().put(appender.getName(), appender);

        firstTest.produceAInfoLog();
        assertThat(logMessages).hasSize(1);
        config.getRootLogger().removeAppender(appender.getName());
        // logMessages.forEach(System.out::println);

    }

    static class MyAppender implements Appender {

        final List<LogEvent> logEvents = Collections.synchronizedList(new ArrayList<>());

        final List<String> logMessages = Collections.synchronizedList(new ArrayList<>());

        public List<LogEvent> getLogEvents() {
            return Collections.unmodifiableList(logEvents);
        }

        public List<String> getLogMessages() {
            return Collections.unmodifiableList(logMessages);
        }

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


        public void reset() {
            logEvents.clear();
            logMessages.clear();
        }
    }
}