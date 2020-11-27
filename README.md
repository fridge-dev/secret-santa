# secret-santa

This is a Secret Santa gift assigner and SMS (text) notification system.

This system will generate random Secret Santa gift assignments for a group of people and send text messages to each person saying who they get to buy a gift for. The system is the only one that knows the assignments, so it will be a secret for everyone!

The target audience of using this software is, well, just me. But maybe someone else finds it useful too!

# How it works

You provide your group's info, and the system takes care of everything.

You provide the name and phone number of each group member, along with optional exclusion rules (e.g. people from the same family shouldn't give each other gifts).

You also provide a message template that will be used to send a customizable SMS based on who is receiving the SMS message and who that person is assigned to give a gift to. There is an example below.

You provide all of this info to the system admin (aka me). The system requires action of a human admin to send all of the SMS messages. But the beauty of it is that the human admin won't even know who is assigned to give a gift to whom!

## Example

Here's **one possible outcome** of the Secret Santa group below.

Recipient | SMS message contents
-|-
Heather | *Secret Santa: Hello Heather, you are assigned to give a present to Diana. Diana's wishlist: 'Heavy metal, chocolate'.*
Brad | *Secret Santa: Hello Brad, you are assigned to give a present to Brylon. Brylon's wishlist: 'Tube socks, jump rope, and skeeter boards'.*
Diana | *Secret Santa: Hello Diana, you are assigned to give a present to Brad. Brad's wishlist: 'I like lifting weights. LoL!'.*
Brylon | *Secret Santa: Hello Brylon, you are assigned to give a present to Zayne. Zayne's wishlist: 'I am really into anything with the color lime-green'.*
Zayne | *Secret Santa: Hello Zayne, you are assigned to give a present to Heather. Heather's wishlist: 'sci-fi novels, baking, ice skating'.*

Corresponding configuration file maintained by the admin:

```
{
  // Customizable template of what each person will be texted. You can change this
  // and send updated info to the group without changing their assignments.
  "messageFormat": "Secret Santa: Hello {self.id}, you are assigned to give a present to {target.id}. {target.id}'s wishlist: '{target.wishlist}'.",

  // The people in the group.
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

  // Pairs of people which are not allowed to give each other gifts.
  "exclusionRules": [
    ["Heather", "Brad"],
    ["Diana", "Brylon"]
  ],

  // Used to deterministically generate a pseudorandom assignment order.
  "rngSeed": 1234
}
```

## Tech details

This runs as a lambda function and sends SMS via AWS SNS. You provide the configuration in the invoke request payload.

## Cost

This is basically free. It costs fractions of a penny per-invocation and per-SMS.

-------------------

# Project planning

### Backlog (somewhat ordered)

1. Fix logging
1. Add ability to text "secret santa of X" instead of only texting "X" or all
1. Use ordered phoneNumbers as hash in RNG seed to ensure different groups of same size don't have same permutation of assignments
1. Enable adding someone to an already created group and disrupting the least amount of assignments
1. Back people/assignments with database to more easily refer to a group

### Done

1. Implement assignment logic
1. Message formatting
1. Combine everything together in app layer
1. Integrate with lambda as entry point
1. Integrate with SMS (still need to test)
