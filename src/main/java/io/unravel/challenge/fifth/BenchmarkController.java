package io.unravel.challenge.fifth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/benchmark")
public class BenchmarkController {
    private static final int TWO_TO_ONE_READ_TO_WRITE_RATIO_ROLL = 67;

    @Autowired
    private DatabaseManager databaseManager;

    @GetMapping
    public String run() throws SQLException {
        int dice = ThreadLocalRandom.current().nextInt(100);
        if (dice < TWO_TO_ONE_READ_TO_WRITE_RATIO_ROLL) {
            Long count = databaseManager.countIssues();
            return "Issues count: " + count;
        } else {
            databaseManager.saveIssue();
            return "Stored a new issue";
        }
    }


}
