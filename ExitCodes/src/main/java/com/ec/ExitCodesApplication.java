package com.ec;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import com.ec.beans.Motor;
import com.ec.exeption.UnknownExeption;

@SpringBootApplication
public class ExitCodesApplication {

	@Bean
	public ExitCodeGenerator exitCodeGenerator() {
		return () -> 188;
		/*
		 * return new ExitCodeGenerator() {
		 * 
		 * @Override public int getExitCode() { return 100; } };
		 */
	}

	@Bean
	public ApplicationListener<ExitCodeEvent> exitCodeEvent() {
		return new ApplicationListener<ExitCodeEvent>() {
			@Override
			public void onApplicationEvent(ExitCodeEvent event) {
				int exitCode = event.getExitCode();
				System.out.println(exitCode);
			}
		};
	}

	// it called after creating the IOC container , in commandLineRunner any
	// Exception rise this will be called by the IOC container becouse we are
	// placing that in the Ioc container
	@Bean
	public ExitCodeExceptionMapper codeExceptionMapper() {
		return new ExitCodeExceptionMapper() {
			@Override
			public int getExitCode(Throwable exception) {
				if (exception.getCause() instanceof UnknownExeption) {
					return -99;
				}
				if (exception.getCause() instanceof RuntimeException) {
					return -234;
				}
				if (exception.getCause() instanceof IllegalAccessException) {
					return -23355;
				}
				return 0;
			}
		};
	}

	@Bean
	public CommandLineRunner commandLineRunners() {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				System.out.println("ExitCodesApplication.commandLineRunners().new CommandLineRunner() {...}.run()");
				throw new IllegalAccessException("unknown Failure");
			}
		};
	}

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ExitCodesApplication.class, args);

		try {
			// if there is a failure it abnormally terminated if we are not write the Try
			// and finally.
			Motor motor = context.getBean("motor", Motor.class);
			motor.start();
		} finally {
			System.exit(SpringApplication.exit(context));
			// int exitCode = SpringApplication.exit(context);
			// System.out.println(exitCode);
			// it passes the exitCode to OperatingSystem
			// System.exit(exitCode);
		}
	}
}
