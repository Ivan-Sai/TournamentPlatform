export interface TeamData {
  id: number,
  name: String,
  currPlayers : number,
  tournamentsId: number[],
  usersId: number[],
  creationDate: Date,
  adminId: number,
  token: string
}
