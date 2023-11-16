import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TeamService} from "../../../../services/TeamService";
import {JwtUtil} from "../../../../util/JwtUtil";

@Component({
  selector: 'app-team-invite-link',
  standalone: true,
  templateUrl: './team-invite-link.component.html',
  styleUrls: ['./team-invite-link.component.css']
})
export class TeamInviteLinkComponent implements OnInit{

  private teamId: number | undefined;
  private userId: number | undefined;
  private teamUUID: string | undefined;

  constructor(private route:ActivatedRoute,
              private teamService:TeamService,
              private jwtUtil: JwtUtil,
              private router: Router
              ) {

  }

  ngOnInit() {
    this.route.params.subscribe(value => {
      this.teamId = value['id']
      this.teamUUID = value['UUID']
    })
    this.userId = this.jwtUtil.getId();
    console.log(this.userId)
    if (this.userId == 0) {
      this.router.navigateByUrl('signup')
      return
    }
    this.teamService.addUser(this.teamId,this.userId,this.teamUUID).subscribe(value => {

      },
      error => {
        if (error.error == 'Invalid team UUID'){
          this.router.navigateByUrl('error/link')
        }
      }
    )
    this.router.navigateByUrl('teams/' + this.teamId).then(() => {
      location.reload();
    });
  }

}
