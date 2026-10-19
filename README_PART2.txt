PROG5121 POE Part 2 - QuickChat

Open this folder in Apache NetBeans using File > Open Project.
It is a Maven Java project targeting JDK 21.

Run:
- Right-click the project > Run
- Right-click the project > Test

Notes:
- Login from Part 1 is retained and the messaging menu is only shown after successful login.
- Stored messages are written to messages.json in the application's working directory.
- The spec's recipient examples conflict: +27718693002 has 12 characters including +, while the task says at most 10 characters. This implementation accepts international numbers beginning with + and containing 8-15 digits, which supports the supplied +27 example.
- MessageTest includes unit tests for IDs, recipient validation, length validation, and hashing.
