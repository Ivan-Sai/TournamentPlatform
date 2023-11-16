export interface UserData {
  id: number,
  username: string,
  email: string,
  teamsIds : number[],
  administratedTournamentsIds: number[],
  tournamentAdmin: boolean,
}
