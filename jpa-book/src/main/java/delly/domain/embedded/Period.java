package delly.domain.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class Period {

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  // Constructors
  protected Period() {
  }

  public Period(LocalDateTime startDate, LocalDateTime endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  // methods
  public boolean isWorking() {
    LocalDateTime now = LocalDateTime.now();
    return now.isAfter(startDate) && now.isBefore(endDate);
  }


  // Getters and Setters

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }
}
