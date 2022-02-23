package com.wavefront.tools.wftop.hypothesis;

import com.google.common.collect.Multimap;
import wavefront.report.ReportPoint;

import java.util.List;

/**
 * A hypothesis to reduce usage.
 *
 * @author Clement Pang (clement@wavefront.com).
 */
public interface Hypothesis {
  /**
   * @return Human understandable description of the hypothesis
   */
  String getDescription();

  List<String> getDimensions();

  Hypothesis cloneHypothesis();

  /**
   * @return The PPS savings of this hypothesis if enacted.
   */
  double getRawPPSSavingsRate(boolean lifetime);

  default double getPPSSavings(boolean lifetime, int numBackends, double sampleRate) {
    return getRawPPSSavingsRate(lifetime) * numBackends / sampleRate;
  }

  double getInstaneousRate();

  /**
   * @return How often is this hypothesis violated (between 0 and 1).
   */
  double getViolationPercentage();

  default double getAdjustedViolationPercentage(long usageLookupDays, double usageFPPRate) {
    return getViolationPercentage();
  }

  /**
   * Process a {@link ReportPoint} and update stats.
   *
   * @return Whether the point was culled by this hypothesis.
   */
  boolean processReportPoint(boolean accessed, String metric, String host,
                             Multimap<String, String> pointTags, long timestamp, double value);

  void reset();

  int getAge();

  void incrementAge();

  void resetAge();
}
