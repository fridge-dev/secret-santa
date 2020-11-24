# secret-santa

This is a Secret Santa assignment manager and text notification system, for my personal use. This system will generate pseudorandom Secret Santa gift assignments (with optional exclusion rules) and broadcast text messages to all people in the group. The system is the only one that knows the assignments, so it will be a secret for everyone!

# How it works

You provide a configuration file in the request payload, and the system takes care of everything.

The configuration file contains the people who are in your Secret Santa group and optional exclusion rules (e.g. Heather and Brad shouldn't be assigned to each other). The configuration file also includes a static RNG seed that is used to generate a deterministic, pseudorandom assignment order. It's important that once you finalize your group, you don't change your group, exclusion rules, or RNG seed, because the gift giving assignments would change as a result.

The configuration file also includes a message template that will be used to generate a customizable message based on who the text is being sent to and who that person is assigned to give a gift. There is an example below.

The system requires action of a human admin to initiate the broadcasting of text messages. But the beauty of it is that human admin won't even know who is assigned to give a gift to whom!

## Example Configuration File

```
{
  "messageFormat": "Secret Santa: Hello {self.id}, you are assigned to give a present to {target.id}. In their own words, here is their wishlist: '{target.wishlist}'.",
  "peopleData": [
    {
      "id": "Heather",
      "phoneNumber": "+12222222222",
      "wishlist": "sci-fi novels, baking, ice skating"
    },
    {
      "id": "Brad",
      "phoneNumber": "+11111111111",
      "wishlist": "I like lifting weights. LoL!"
    },
    {
      "id": "Diana",
      "phoneNumber": "+13333333333",
      "wishlist": "Heavy metal, chocolate"
    },
    {
      "id": "Brylon",
      "phoneNumber": "+14444444444",
      "wishlist": "Tube socks, jump rope, and skeeter boards"
    }
    {
      "id": "Zayne",
      "phoneNumber": "+15555555555",
      "wishlist": "I am really into anything with the color lime-green"
    }
  ],
  "exclusionRules": [
    ["Heather", "Brad"],
    ["Diana", "Brylon"]
  ],
  "rngSeed": 1234
}
```

## Tech details

This runs as a lambda function and sends SMS via AWS SNS. You provide the configuration in the invoke request payload.

-------------------

# Project planning

### To do

1. Fix logging
1. Enable adding someone to an already created group and disrupting the least amount of assignments

### Done

1. Implement assignment logic
1. Message formatting
1. Combine everything together in app layer
1. Integrate with lambda as entry point
1. Integrate with SMS (still need to test)

### Out of scope

1. Back people/assignments with database to more easily refer to a group
