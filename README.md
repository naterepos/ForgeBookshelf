# AdvancedSpawns

## Commands
 - /advancedspawns help - (`advancedspawns.command.help`)
 - /advancedspawns reload - (`advancedspawns.command.reload`)
 - /advancedspawns createroute <route> <maxspawns> <x> <y> <z> <radius> - (`advancedspawns.command.createroute`)
 - /advancedspawns deleteroute <route> - (`advancedspawns.command.deleteroute`)
 - /advancedspawns listroutes - (`advancedspawns.command.listroutes`)
 - /advancedspawns addpokemon <route> <alias> <rarity> - (`advancedspawns.command.addpokemon`)
___

## Configuration

### Zones
- Each zone is represented by a `Vector` (x,y,z value) and a radius where Pokémon can spawn 
- A zone possesses a `SpawnType` which allows only a particular type of Pokémon to spawn 
  - Types: `LAND`, `WATER`, `AIR`
- The `Pixelmon` list allows you to point to several Pokémon aliases located in the `pokemon.conf` file
  - Each of these have a weight to indicate how often they should spawn
    - The higher the weight, the more often the Pokémon will spawn
- To limit spawning, set `MaxSpawns` to a reasonable number within the given `BoundingBox`
- To allow for day/night limits, the `Time` option allows you to pick either "Day" or "Night"
  - This is an optional value and can be omitted
### Pokemon
- A Pokémon is a custom Pokémon that can be deserialized to a physical entity in the game
- Some settings for a custom Pokémon are removable while others are vital
- Vital
  - `Species`: the type of Pokémon
  - `Level`: the range of levels allowed for this Pokémon
  - `Unbreedable`: a boolean (true/false) to say whether this Pokémon is unbreedable or not
  - `Alias`: the alias attached to the route(s) this Pokémon will spawn in
- Removable
  - `EVs and IVs`: a map of ev/iv string identifiers and integer values (0-31 for ivs), (0-255 for evs)
  - `SpawnTime`: Day or Night
  - `Nature`
  - `Nickname`: custom nickname that will automatically update with evolutions to the next evolution name
  - `Form`: special form for pixelmon