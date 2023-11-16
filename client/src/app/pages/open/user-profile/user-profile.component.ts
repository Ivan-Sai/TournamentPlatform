import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {JwtUtil} from "../../../util/JwtUtil";
import {UserData} from "../../../models/request/UserData";
import {PersonalService} from "../../../services/PersonalService";
import {AsyncPipe, NgForOf, NgIf, NgStyle} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Observable} from "rxjs";
import {TeamService} from "../../../services/TeamService";
import { TeamData } from 'src/app/models/request/TeamData';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  templateUrl: './user-profile.component.html',
  imports: [
    NgStyle,
    NgIf,
    FormsModule,
    ReactiveFormsModule,
    AsyncPipe,
    NgForOf
  ],
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  public userId: number | undefined;
  public isOwner: boolean | undefined;
  public userData: UserData | undefined;
  public imageUrl: string | undefined;
  public isHasImage: boolean = false;
  public uploadImage: File | undefined;

  public avatarWindowOpen: boolean = false;
  public leaveTeamWindowOpen : boolean = false;
  public teams$ : Observable<TeamData[]> | undefined;

  public logoDataMap: { [key: number]: string } = {};
  public isHasLogoMap: {[key: number] : boolean} = {}
  public isLoaded = false;
  public leaveTeamName: string | undefined;
  public leaveTeamId: number | undefined;



  constructor(private route: ActivatedRoute,
              private jwtUtil: JwtUtil,
              private personalService:PersonalService,
              private teamService: TeamService,
              private router:Router,
  ) {


    this.route.params.subscribe(value => {
      this.userId = value['id']
      this.ngOnInit()
    })

  }


  ngOnInit(): void {
    this.isOwner = this.jwtUtil.getId() == this.userId;
    this.isLoaded = false;
    this.personalService.getUserData(this.userId).subscribe(value => {
      this.userData = value;
    },error => {
      if (error.error == "User not found") {
        this.router.navigateByUrl('error/404')
      }
    })
    this.teams$ = this.personalService.getTeamsByPersonal(this.userId);
    this.loadLogos();
    this.personalService.getAvatar(this.userId).subscribe( res =>{
        this.imageUrl = 'data:image/jpeg;base64,' + res.data;
        this.isHasImage = true;

      },error => {
      this.isHasImage = false;
      }
    );
    setTimeout(() => {
      this.isLoaded = true;
    }, 500);
  }



  changeAvatar(){
    if (this.isOwner)
    this.avatarWindowOpen = true
  }

  public onImageUpload(event:Event) {
    if (event && event.target){
      // @ts-ignore
      this.uploadImage = event.target.files[0];
    }
  }
  public submitImage() {
    this.avatarWindowOpen = false;
    this.isHasImage = true;
    this.personalService.setPersonalAvatar(this.uploadImage, this.userId).subscribe(() =>{
        this.personalService.getAvatar(this.userId).subscribe( res =>{
            this.imageUrl = 'data:image/jpeg;base64,' + res.data;
            console.log(this.imageUrl)
          })
      }
    )
  }

  loadLogos(): void {
    if (this.teams$)
    this.teams$.forEach(value => {
      value.forEach(value1 => {
        this.teamService.getTeamLogo(value1.id).subscribe(
          value2 => {
            this.logoDataMap[value1.id] = 'data:image/jpeg;base64,' + value2.data
            this.isHasLogoMap[value1.id] = true;
          }
          ,error => {
            this.isHasLogoMap[value1.id] = false
          }
        )
      })
    })

  }
  getLogo(id: number): string {
    return this.logoDataMap[id]
  }

  redirectToPage(url:string){
    this.router.navigateByUrl(url);
  }

  openLeaveTeamWindow(name: String, id: number) {
    this.leaveTeamWindowOpen = true;
    this.leaveTeamName = name.toString();
    this.leaveTeamId = id;
  }
  closeLeaveWindow () {
    this.leaveTeamWindowOpen = false;
    this.leaveTeamName = undefined;
    this.leaveTeamId = undefined
  }

  leaveTeam() {
    this.teamService.leaveTeam(this.leaveTeamId,this.userId).subscribe(
      () => {
        location.reload();
      }
    );
  }

}
