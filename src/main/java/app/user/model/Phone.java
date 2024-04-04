package app.user.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="phone")
public class Phone {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
	@JsonIgnore
    private UUID uuid;
	
	private String number;
	
	@JsonProperty("citycode")
	private String cityCode;
	
	@JsonProperty("contrycode")
	private String countryCode;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
}
