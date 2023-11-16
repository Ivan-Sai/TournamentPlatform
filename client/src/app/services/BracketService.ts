import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {BracketData} from "../models/request/BracketData";

@Injectable({
  providedIn: 'root'
})
export class BracketService {

  constructor(private http: HttpClient) {
  }

  public findBracket(id: number| undefined) {
    return this.http.get<BracketData>('http://localhost:8080/api/open/tournament/bracket/' + id)
  }

  teamWon(teamId: number, bracketId: number | undefined, matchId: number, stageId: number) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))

    return this.http.post('http://localhost:8080/api/private/admin/tournament/bracket/' + bracketId+'/stage/'+stageId+ '/match/' + matchId + '/team/' + teamId, {}, {headers: headers})
  }
}
