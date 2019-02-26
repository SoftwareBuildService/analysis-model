package edu.hm.hafner.analysis.parser.violations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import edu.hm.hafner.analysis.Report;

import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.CPPCheckParser;

/**
 * Parses CPPCheck files.
 *
 * @author Ullrich Hafner
 */
public class CppCheckAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 2244442395053328008L;

    @Override
    protected CPPCheckParser createParser() {
        return new CPPCheckParser();
    }

    @Override
    protected Report convertToReport(final List<Violation> violations) {
        final Map<String, List<Violation>> violationsPerGroup =
                violations.stream().collect(Collectors.groupingBy(Violation::getGroup));

        Report report = new Report();
        for (List<Violation> group : violationsPerGroup.values()) {
            IssueBuilder issueBuilder = createIssueBuilder(group.get(0));
            LineRangeList lineRanges = new LineRangeList();
            for (int i = 1; i < group.size(); i++) {
                Violation violation = group.get(i);
                lineRanges.add(new LineRange(violation.getStartLine()));
            }
            issueBuilder.setLineRanges(lineRanges);
            report.add(issueBuilder.build());
        }
        return report;
    }
}
