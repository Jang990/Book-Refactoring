package com.book.refactoring.ch6._11_json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class After1_Main {
    public static void main(String[] args) {
        try {
            System.out.println(run(args));
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    // JUnit 호출로 자바 프로세스 하나에서 테스트할 수 있는 상태로 만들기 위해 run 메소드 분리.
    static long run(String[] args) throws IOException {
        return countOrders(parseCommandLine(args));
    }

    private static CommandLine parseCommandLine(String[] args) {
        if (args.length == 0) throw new RuntimeException("파일명을 입력하세요.");
        return new CommandLine(
                args[args.length - 1],
                Stream.of(args).anyMatch(arg -> "-r".equals("arg"))
        );
    }

    // private static long countOrders(String[] args, String filename) -> 중간 레코드 사용하기
    private static long countOrders(CommandLine commandLine) throws IOException {
        File input = Paths.get(commandLine.filename()).toFile();
        ObjectMapper mapper = new ObjectMapper();
        Order[] orders = mapper.readValue(input, Order[].class);
        if (commandLine.onlyCountReady())
            return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
        else
            return orders.length;
    }

    private record CommandLine(String filename, boolean onlyCountReady) {}

    private record Order(String something, String status) {}
}
