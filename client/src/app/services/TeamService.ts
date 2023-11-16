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
import {TeamData} from "../models/request/TeamData";
import {UserData} from "../models/request/UserData";

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  constructor(private http:HttpClient,
              private jwtUtil: JwtUtil,
  ) { }



  getTeamLogo(teamId: number | undefined) {
    return this.http.get<ImageData>('http://localhost:8080/api/open/team/' + teamId + '/logo')
  }

  createTeam(name: string, uploadImage: File | undefined) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    const formData = new FormData();
    if (uploadImage) formData.append('image', uploadImage, uploadImage.name)
    formData.append('name', name)
    // @ts-ignore
    formData.append('ownerId', this.jwtUtil.getId().toString())
    return this.http.post<number>('http://localhost:8080/api/private/personal/teams',formData,{headers: headers})
  }

  findById(teamId: number | undefined) {
    return this.http.get<TeamData>('http://localhost:8080/api/open/team/'+teamId);
  }

  setTeamLogo(image: File | undefined, teamId: number | undefined) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))

    const formData = new FormData();
    if (image) formData.append('image', image, image.name)
    return this.http.put<ResponseData<any>>('http://localhost:8080/api/private/personal/teams/'+ teamId +'/logo', formData, {headers: headers});
  }

  getUsersByTeam(teamId: number | undefined):Observable<UserData[]> {
    return this.http.get<UserData[]>('http://localhost:8080/api/open/team/'+teamId + '/users');
  }

  addUser(teamId: number | undefined, userId: number | undefined, teamUUID: string | undefined) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    return this.http.put('http://localhost:8080/api/private/personal/' + userId +'/inviteToTeam/' + teamId+ '/' + teamUUID, {},{headers: headers})
  }

  deleteTeam(teamId: number | undefined) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    const userId = this.jwtUtil.getId()
    return this.http.delete('http://localhost:8080/api/private/personal/' + userId +'/teams/' + teamId,{headers: headers})

  }

  leaveTeam(leaveTeamId: number | undefined, userId: number | undefined) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    return this.http.put('http://localhost:8080/api/private/personal/' + userId + '/teams/' + leaveTeamId, {}, {headers: headers})
  }
}
