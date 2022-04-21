package ru.netology.data;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    private final String city;
    private final String name;
    private final String phone;

}
