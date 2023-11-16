import {Component, OnInit} from '@angular/core';
import {TeamService} from "../../../../services/TeamService";
import {ActivatedRoute, Router} from "@angular/router";
import { TeamData } from 'src/app/models/request/TeamData';
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {JwtUtil} from "../../../../util/JwtUtil";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Observable} from "rxjs";
import {UserData} from "../../../../models/request/UserData";
import {PersonalService} from "../../../../services/PersonalService";


@Component({
  selector: 'app-team-page',
  standalone: true,
  templateUrl: './team-page.component.html',
  imports: [
    NgIf,
    FormsModule,
    ReactiveFormsModule,
    AsyncPipe,
    NgForOf
  ],
  styleUrls: ['./team-page.component.css']
})
export class TeamPageComponent implements OnInit {


  teamId: number | undefined;
  teamUUID: string | undefined;
  team: TeamData | undefined;
  public userInviteLink: string| undefined;

  public isOwner: boolean = false;
  public avatarWindowOpen: boolean = false
  public deleteTeamWindowOpen: boolean = false
  public addPlayerWindowOpen : boolean = false;
  public kickPlayerWindowOpen: boolean = false;
  public leaveTeamWindowOpen : boolean = false;
  public isLoaded: boolean = false;

  uploadImage : File | undefined;
  imageUrl: string | undefined;
  isHasImage: boolean = false;

  users$: Observable<UserData[]>| undefined;
  public logoDataMap: { [key: number]: string } = {};
  public isHasLogoMap: {[key: number] : boolean} = {}
  public isCopied: boolean = false;
  public userId: number| undefined;
  public kickUserId: number | undefined;
  public kickUserName: string | undefined;


  constructor(private teamService:TeamService,
              private route: ActivatedRoute,
              private jwtUtil:JwtUtil,
              private router: Router,
              private personalService:PersonalService,
              ) {
    this.route.params.subscribe(value => {
      this.teamId = value['id']
    })

  }

  ngOnInit(): void {
    this.teamService.getTeamLogo(this.teamId).subscribe( res =>{
        this.imageUrl = 'data:image/jpeg;base64,' + res.data;
        this.isHasImage = true;
      }
    )
    this.teamService.findById(this.teamId).subscribe(value => {
      this.team = value
      this.isOwner = value.adminId == this.jwtUtil.getId();
      this.teamUUID = value.token;
      this.userInviteLink= `http://localhost:4200/teams/${this.teamId}/${this.teamUUID}/inviteLink`
      },error => {
      if (error.error == "Team not found") {
        this.router.navigateByUrl('error/404')
      }
    }
    )
    this.users$ = this.teamService.getUsersByTeam(this.teamId)
    this.loadLogos()
    setTimeout(() => {
      this.isLoaded = true;
    }, 500);
    this.userId = this.jwtUtil.getId();
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
    this.teamService.setTeamLogo(this.uploadImage, this.teamId).subscribe(() =>{
        this.teamService.getTeamLogo(this.teamId).subscribe( res =>{
            this.imageUrl = 'data:image/jpeg;base64,' + res.data;
            this.isHasImage = true;
          }
        )
      }

    )
  }

  loadLogos(): void {
    if (this.users$)
      this.users$.forEach(value => {
        value.forEach(value1 => {
          this.personalService.getAvatar(value1.id).subscribe(
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

  addPlayer() {
    if (this.isOwner) {
      this.addPlayerWindowOpen = true

    }
  }
  copyInviteLink() {
    if (this.userInviteLink)
    navigator.clipboard.writeText(this.userInviteLink)
    this.isCopied = true;
    setTimeout(() => {
      this.isCopied = false;
    }, 500);
  }

  openDeleteTeamWindow() {
    this.deleteTeamWindowOpen = true;
  }
  closeDeleteWindow () {
    this.deleteTeamWindowOpen = false;
  }

  deleteTeam() {
    this.teamService.deleteTeam(this.teamId).subscribe(
      () => {
        this.router.navigateByUrl('tournaments?filter=upcoming')
      }
    );
  }

  openKickPlayerWindow(username: string, id: number){
    this.kickPlayerWindowOpen = true;
    this.kickUserId = id;
    this.kickUserName = username;
  }
  closeKickPlayerWindow(){
    this.kickPlayerWindowOpen = false;
    this.kickUserId = undefined;
    this.kickUserName = undefined;
  }

  kickPlayer() {
    this.teamService.leaveTeam(this.teamId,this.kickUserId).subscribe(() =>location.reload())
  }

  openLeaveTeamWindow() {
    this.leaveTeamWindowOpen = true;
  }
  closeLeaveWindow () {
    this.leaveTeamWindowOpen = false;
  }

  leaveTeam() {
    this.teamService.leaveTeam(this.teamId,this.userId).subscribe(
      () => {
        location.reload();
      }
    );
  }
}
