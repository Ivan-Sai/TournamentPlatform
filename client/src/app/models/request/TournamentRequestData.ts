export interface TournamentRequestData {
  name: string,
  discipline: string,
  maxTeams: number,
  minTeams: number,
  currTeams : number,
  creationDate: Date,
  startDate: Date,
  adminId: number,
  tournamentType: string,
  isStarted: boolean,
  isCanceled: boolean,
  id: number,
  bracketId: number,
  winnerId: number
}
