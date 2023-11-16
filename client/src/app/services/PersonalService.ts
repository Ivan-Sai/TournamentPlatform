import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {JwtUtil} from "../util/JwtUtil";
import {Observable} from "rxjs";
import {TeamData} from "../models/request/TeamData";
import {UserData} from "../models/request/UserData";
import {ImageData} from "../models/request/ImageData";
import {ResponseData} from "../models/ResponseData";

@Injectable({
  providedIn: 'root'
})
export class PersonalService {

  constructor(private http: HttpClient,
              private jwtUtil: JwtUtil) {
  }

  getTeamsByPersonal(id: number | undefined): Observable<TeamData[]> {
    return this.http.get<TeamData[]>('http://localhost:8080/api/open/user/'+ id +'/teams')
  }

  getUserData(userId: number | undefined) {
    return this.http.get<UserData>('http://localhost:8080/api/open/user/'+ userId)
  }

  getAvatar(userId: number | undefined) {
    return this.http.get<ImageData>('http://localhost:8080/api/open/user/' + userId + '/logo')
  }

  setPersonalAvatar(image: File | undefined, userId: number | undefined) {
    const headers = new HttpHeaders().set("Authorization", "Bearer " + localStorage.getItem("accessToken"))

    const formData = new FormData();
    if (image) formData.append('image', image, image.name)
    return this.http.put<ResponseData<any>>('http://localhost:8080/api/private/personal/'+ userId +'/avatar', formData, {headers: headers});
  }
}
