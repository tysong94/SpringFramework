package kr.ac.kopo.board.web.resort;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.ac.kopo.board.domain.Reservation;
import kr.ac.kopo.board.domain.Room;
import kr.ac.kopo.board.domain.User;
import kr.ac.kopo.board.service.ReservationService;
import kr.ac.kopo.board.service.RoomService;
import kr.ac.kopo.board.service.UserService;
import kr.ac.kopo.board.vo.ReservationVO;

@Controller
public class ResortController {
	
	private static final Logger logger = LoggerFactory.getLogger(ResortController.class);
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private UserService userService;
	
	//-----a--------------------------------------------------------	
	// ���� ������
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		return "resort/main";
	}
	
	
	// ���� ������
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(Locale locale, Model model) {
		
		return "resort/main";
	}
	
	// VIP��
	@RequestMapping(value = "/vip", method = RequestMethod.GET)
	public String vip(Locale locale, Model model) {
		
		return "resort/a1_vip";
	}
	
	// �Ϲݷ�
	@RequestMapping(value = "/common", method = RequestMethod.GET)
	public String common(Locale locale, Model model) {
		
		return "resort/a2_common";
	}

	// �ո����η�
	@RequestMapping(value = "/reasonable", method = RequestMethod.GET)
	public String reasonable(Locale locale, Model model) {
		
		return "resort/a3_reasonable";
	}

	//-----b--------------------------------------------------------
	// ã�ƿ��±�
	@RequestMapping(value = "/way", method = RequestMethod.GET)
	public String way(Locale locale, Model model) {
		
		return "resort/b1_way";
	}
	// ���߱���
	@RequestMapping(value = "/publicc", method = RequestMethod.GET)
	public String publicc(Locale locale, Model model) {
		
		return "resort/b2_public";
	}
	// �ڰ���
	@RequestMapping(value = "/own", method = RequestMethod.GET)
	public String own(Locale locale, Model model) {
		
		return "resort/b3_own";
	}
	
	//-----�������--------------------------------------------------------
	// ã�ƿ��±�
	@RequestMapping(value = "/attractions1", method = RequestMethod.GET)
	public String attractions1(Locale locale, Model model) {
		
		return "resort/c1_attractions";
	}
	// ���߱���
	@RequestMapping(value = "/attractions2", method = RequestMethod.GET)
	public String attractions2(Locale locale, Model model) {
		
		return "resort/c2_attractions";
	}
	// �ڰ���
	@RequestMapping(value = "/attractions3", method = RequestMethod.GET)
	public String attractions3(Locale locale, Model model) {
		
		return "resort/c3_attractions";
	}
	
	//-----����--------------------------------------------------------
	// ���� ����Ʈ
	@RequestMapping(value = "/reservationList", method = RequestMethod.GET)
	public String reservationList(Locale locale, @RequestParam Map<String, String> param, Model model) {
		
		List<ReservationVO> reservationVOs = reservationService.setReservations();
		model.addAttribute("reservationVOs", reservationVOs);

		return "resort/d1_reservationList";
	}
	
	// ���� ȭ��
	@RequestMapping(value = "/reservationForm", method = RequestMethod.GET)
	public String reservationForm(Locale locale, @RequestParam Map<String, String> param, Model model) throws ParseException {
		// ��ü ����Ʈ ��������(�����, ��¥, �����)
		List<User> all_users = userService.selectAll();
		List<String> all_dates = reservationService.dates();
		List<Room> all_rooms = roomService.selectAll();
		
		// ��¥, �� �Ķ���� �ޱ�
		String reserve_date = param.get("date");
		int reserve_room = Integer.parseInt(param.get("room"));
		
		model.addAttribute("all_users", all_users);
		model.addAttribute("all_dates", all_dates);
		model.addAttribute("reserve_date", reserve_date);
		model.addAttribute("all_rooms", all_rooms);
		model.addAttribute("reserve_room", reserve_room);
		
		return "resort/d2_reservationForm";
	}
	
	// ���� ����
	@RequestMapping(value = "/reservation", method = RequestMethod.POST)
	public String reservation(Locale locale, @RequestParam Map<String, String> param, Model model) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		// ������, ���೯¥, �����, ���� �� �Ķ���� �ޱ�
		User user = userService.selectOne(Long.parseLong(param.get("user")));
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(param.get("date"));
		Room room = roomService.selectOne(Integer.parseInt(param.get("room")));
		String comment = param.get("comment");
		
		if(reservationService.selectOneByDateRoom(date, Integer.parseInt(param.get("room"))) == null) {
			Reservation reservation = new Reservation();
			reservation.setUser(user);
			reservation.setDate(date);
			reservation.setRoom(room);
			reservation.setComment(comment);
			reservationService.createOne(reservation);
		} else {
			model.addAttribute("msg", "������ á���ϴ�.");
			return "resort/alert";
		}
		
		return "redirect:/reservationList";
	}
	
}
