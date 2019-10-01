package com.ams.search.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
public class Flight
{

	@Id
	@GeneratedValue(generator = "flight-uuid")
	@GenericGenerator(name = "flight-uuid", strategy = "uuid2")
	private String id;

	@NotNull
	@NonNull
	private String flightNumber;

	@NotNull
	@NonNull
	private String origin;

	@NotNull
	@NonNull
	private String destination;

	@NotNull
	@NonNull
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate flightDate;

	@NotNull
	@NonNull
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fare_id")
	private Fare fare;

	@NotNull
	@NonNull
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "inv_id")
	private Inventory inventory;
}
