package com.backend.reditclone.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {

	private String Subject;
	private String recipient;
	private String body;
}
