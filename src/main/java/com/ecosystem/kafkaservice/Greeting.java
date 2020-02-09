package com.ecosystem.kafkaservice;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Greeting {
    private String msg;
    private String name;
}
