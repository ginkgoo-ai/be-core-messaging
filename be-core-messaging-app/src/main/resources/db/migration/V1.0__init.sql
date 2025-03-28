-- Create email related tables
------------------------------------------

-- Email表
DROP TABLE IF EXISTS emails CASCADE;
CREATE TABLE emails (
                        id VARCHAR(36) PRIMARY KEY,
                        sender VARCHAR(255) NOT NULL,
                        receiver VARCHAR(255) NOT NULL,
                        cc VARCHAR(255),
                        bcc VARCHAR(255),
                        subject VARCHAR(255) NOT NULL,
                        content TEXT,
                        email_type VARCHAR(100) NOT NULL,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP WITH TIME ZONE
);

-- 创建索引
CREATE INDEX idx_email_sender ON emails USING hash (sender);
CREATE INDEX idx_email_receiver ON emails USING hash (receiver);
CREATE INDEX idx_email_created ON emails USING btree (created_at DESC);

-- Attachment表
DROP TABLE IF EXISTS attachments CASCADE;
CREATE TABLE attachments (
                             id VARCHAR(36) PRIMARY KEY,
                             filename VARCHAR(255) NOT NULL,
                             path TEXT NOT NULL,
                             size BIGINT NOT NULL,
                             content_type VARCHAR(100),
                             email_id VARCHAR(36) NOT NULL,
                             created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP WITH TIME ZONE,
                             CONSTRAINT fk_attachment_email FOREIGN KEY (email_id) REFERENCES emails(id) ON DELETE CASCADE
);

-- 创建索引
CREATE INDEX idx_attachment_email ON attachments USING hash (email_id);
CREATE INDEX idx_attachment_created ON attachments USING btree (created_at DESC);

-- EmailTemplate表
DROP TABLE IF EXISTS email_templates CASCADE;
CREATE TABLE email_templates (
                                 id VARCHAR(36) PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL,
                                 description VARCHAR(255),
                                 email_type VARCHAR(100) NOT NULL,
                                 subject VARCHAR(255) NOT NULL,
                                 content TEXT,
                                 properties TEXT,
                                 created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 updated_at TIMESTAMP WITH TIME ZONE
);

-- 创建索引
CREATE INDEX idx_template_name ON email_templates USING hash (name);
CREATE INDEX idx_template_type ON email_templates USING hash (email_type);
CREATE INDEX idx_template_created ON email_templates USING btree (created_at DESC);