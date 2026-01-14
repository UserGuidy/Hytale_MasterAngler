
    Hytale Modding - Technical Context for Jules

    1. Programming Languages: * Server-side: Java (Use for DynamicRod logic, XP persistence, and server authority). * Client-side: C# (Use for UI tension gauge and client-predictive visual feedback).

2. Data Handling: * Format: Strict JSON for all components and fish definitions. * Paths: /data/components/rods/ and /data/fish/.

3. Core Logic Requirements: * Server Authoritative: All catch validations and XP gains must happen on the server. * Networking: Use Hytale's standard packet system for syncing the tension gauge between the DynamicRod (server) and the UI (client).

4. Assets:

    Models are created in Hytale Model Maker. The code should reference model IDs for the 4 modular components: Body, Line, Reel, and Bait .