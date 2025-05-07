package com.ginkgooai.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Types of email templates available in the system")
public enum EmailType {

	@Schema(description = "Email template for sending invitations")
	INVITATION,

	@Schema(description = "Email template for inviting users to upload content")
	INVITE_UPLOAD,

	@Schema(description = "Email template for share shortlist")
	SHARE_SHORTLIST

}
