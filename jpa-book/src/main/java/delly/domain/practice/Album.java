package delly.domain.practice;

import jakarta.persistence.Entity;

@Entity
public class Album extends Item {

  private String artist;

  private String etc;

  public String getArtist() {
    return artist;
  }

  public String getEtc() {
    return etc;
  }
}
