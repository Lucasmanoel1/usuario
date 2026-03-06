package com.lucasmanoel.usuario.business.dto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelefoneDTO {


    private String numero;
    private String ddd;
}
