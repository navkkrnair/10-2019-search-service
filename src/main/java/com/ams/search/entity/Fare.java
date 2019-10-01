package com.ams.search.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
public class Fare
{

	@Id
	@GeneratedValue(generator = "fare-uuid")
	@GenericGenerator(name = "fare-uuid", strategy = "uuid2")
	private String id;

	@NotNull
	@NonNull
	private String amount;

	@NotNull
	@NonNull
	private String currency;
}
