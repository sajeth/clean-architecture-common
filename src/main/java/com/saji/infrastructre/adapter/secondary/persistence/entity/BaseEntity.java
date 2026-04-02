package com.saji.infrastructre.adapter.secondary.persistence.entity;


import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public abstract class BaseEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("active")
    private Boolean active = true;

    @Version
    private Long version;

    @LastModifiedBy
    @Column("modified_by")
    private String modifiedBy;

    @LastModifiedDate
    @Column("modified_date")
    private LocalDateTime modifiedDate;

    @CreatedBy
    @Column("created_by")
    private String createdBy;

    @CreatedDate
    @Column("created_date")
    private LocalDateTime createdDate;


    /**
     * Generates a unique URL-friendly slug based on the entity's name and id.
     *
     * @return a lowercase, hyphen-separated string with special characters removed,
     *         suffixed with the entity's id to ensure uniqueness
     */
    public String generateSlug() {
        if (this.name == null || this.name.isBlank()) {
            return this.id != null ? this.id.toString() : "";
        }

        // Normalize to decompose accented characters (e.g., é -> e + combining accent)
        String normalized = Normalizer.normalize(this.name, Normalizer.Form.NFD);

        // Remove diacritical marks (accents)
        Pattern diacriticsPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutDiacritics = diacriticsPattern.matcher(normalized).replaceAll("");

        String base = withoutDiacritics
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")  // Remove special characters except spaces and hyphens
                .replaceAll("\\s+", "-")           // Replace spaces with hyphens
                .replaceAll("-+", "-")             // Replace multiple hyphens with single hyphen
                .replaceAll("^-|-$", "");          // Remove leading/trailing hyphens

        String suffix = this.id != null ? this.id.toString() : UUID.randomUUID().toString();
        return base.isEmpty() ? suffix : base + "-" + suffix;
    }
}
