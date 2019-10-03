package org.skywind;

        import org.apache.commons.io.IOUtils;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Created at 29/09/2019.
 */
public class ReadMe {

    public static void main(String[] args) throws IOException {
        List<String> lines = IOUtils.readLines(ReadMe.class.getResourceAsStream("/completed.csv"));
        List<BookInfo> bookInfos = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        BookInfo.State state = BookInfo.State.DONE;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (state == BookInfo.State.DONE) {
                    state = BookInfo.State.IN_PROGRESS;
                } else if (state == BookInfo.State.IN_PROGRESS) {
                    state = BookInfo.State.TODO;
                }
            } else {
                BookInfo book = new BookInfo(line);
                book.setState(state);
                bookInfos.add(book);
            }
        }

        sb.append("Читаю сейчас:\n");
        sb.append("--\n\n");
        bookInfos.stream().filter(b -> b.getState() == BookInfo.State.IN_PROGRESS).forEach(bi -> {
            sb.append(Formatter.format(bi, false)).append("\n");
        });
        sb.append("\n\n");

        sb.append("Последние прочитанные книги:\n");
        sb.append("--\n\n");
        long totalRead = bookInfos.stream().filter(b -> b.getState() == BookInfo.State.DONE).count();
        bookInfos.stream().skip(totalRead - 10).limit(10).forEach(bi -> {
            sb.append(Formatter.format(bi, false)).append("\n");
        });
        sb.append("\n\n");

        String term = ("" + Calendar.getInstance().get(Calendar.YEAR)).substring(2);
        long cnt = lines.stream().filter(l -> l.contains(term)).count();
        sb.append("Прочитано в этом году: ").append(cnt).append("\n");
        sb.append("--\n\n");


        sb.append("Рекоммендую:\n");
        sb.append("--\n\n");

        bookInfos.stream().filter(i -> "2".equals(i.score)).forEach(book -> {
            sb.append(Formatter.format(book, false));
            sb.append("\n");
        });

        System.out.println(sb.toString());
    }
}