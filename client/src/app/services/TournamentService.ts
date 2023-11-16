import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {ResponseData} from "../models/ResponseData";
import {RegisterData} from "../models/request/RegisterData";
import {FormControl, Validators} from "@angular/forms";
import {JwtUtil} from "../util/JwtUtil";
import {TournamentRequestData} from "../models/request/TournamentRequestData";
import {ImageData} from "../models/request/ImageData";
import {AllTournamentRequestData} from "../models/request/AllTournamentRequestData";

@Injectable({
  providedIn: 'root'
})
export class TournamentService {

  constructor(private http:HttpClient,
              private jwtUtil: JwtUtil,
  ) { }


  public create(formValues: any): Observable<number> {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))

    const name: string = formValues['name'];
    const discipline: string = formValues['discipline'];
    const maxTeams: number = formValues['maxTeams'];
    const minTeams: number = formValues['minTeams'];
    const startDate: Date = formValues['startDate'];
    const tournamentType: string = formValues['tournamentType'];
    const adminId :number | undefined= this.jwtUtil.getId();

    return this.http.post<ResponseData<number>>('http://localhost:8080/api/private/admin/tournament', {
      name, discipline, maxTeams, minTeams, startDate, adminId,tournamentType
    },{headers: headers}).pipe(
      map(res => res.data)
    )
  }
  public setTournamentImage(image: File | undefined, id: number | undefined){
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))

    const formData = new FormData();
    if (image) formData.append('image', image, image.name)
    return this.http.put<ResponseData<any>>('http://localhost:8080/api/private/admin/tournament/'+ id +'/image', formData, {headers: headers});
  }

  public getTournament(id:number | undefined): Observable<TournamentRequestData> {
    return this.http.get<ResponseData<TournamentRequestData>>('http://localhost:8080/api/open/tournament/' + id)
      .pipe(
        map(res => res.data)
      )
  }

  getTournamentLogo(tournamentId: number | undefined) {
    return this.http.get<ImageData>('http://localhost:8080/api/open/tournament/' + tournamentId + '/logo')
  }

  deleteTournament(tournamentId: number | undefined) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    return this.http.delete('http://localhost:8080/api/private/admin/tournament/' + tournamentId, {headers : headers})
  }

  cancelTournament(tournamentId: number | undefined) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    return this.http.put('http://localhost:8080/api/private/admin/tournament/cancel/' + tournamentId, {},{headers: headers})
  }

  getALlTournaments(filter: string|null, discipline: string | null): Observable<TournamentRequestData[]> {
    return this.http.get<TournamentRequestData[]>('http://localhost:8080/api/open/tournament?filter=' + filter + '&discipline=' + discipline)
  }

  addTeam(tournamentId: number | undefined, teamId: number | undefined) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    return this.http.put('http://localhost:8080/api/private/personal/tournament/' + tournamentId+ '/team/' + teamId,{},{headers: headers})
  }
}
