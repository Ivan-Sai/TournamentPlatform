import {TeamData} from "./TeamData";

export interface MatchData {
  matchId: number,
  team1: TeamData,
  team2: TeamData,
  winnerId: number
}
