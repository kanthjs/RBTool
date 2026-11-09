/**
 * Creates the Google Form and the response Google Sheet for RBTool feedback and problem reports.
 *
 * How to use:
 * 1. Open https://script.new while signed in to your Google account.
 * 2. Delete the starter code and paste this whole file.
 * 3. Click Save, then Run (createRBToolFeedbackForm).
 * 4. Accept the authorization request (Review Permissions > Advanced > Go to project > Allow).
 * 5. Read the Execution log: it prints the public form link. Send that link to the maintainer, who sets
 *    RBTOOL_FEEDBACK_URL (or feedback_form_url in prototype/app/app.R) to it.
 */

function createRBToolFeedbackForm() {
  var form = FormApp.create('RBTool - แบบฟอร์มส่งข้อเสนอแนะและแจ้งปัญหา');

  form.setTitle('RBTool - แบบฟอร์มรับฟังข้อเสนอแนะและแจ้งปัญหาการใช้งาน')
      .setDescription(
        'แบบฟอร์มนี้รวบรวมความคิดเห็น ปัญหาที่พบ (Bug) และฟังก์ชันที่อยากให้เพิ่ม ' +
        'สำหรับ RBTool (Rice Breeding Tool) เว็บแอปวิเคราะห์ข้อมูลการทดลองปรับปรุงพันธุ์พืช ' +
        'ตามแนวทางของ PBTools\n\n' +
        'ขอบคุณทุกความคิดเห็นที่ช่วยพัฒนาเครื่องมือนี้'
      )
      .setAllowResponseEdits(false)
      .setCollectEmail(false);

  // 1. Type of message
  form.addMultipleChoiceItem()
      .setTitle('1. ประเภทของข้อความที่ต้องการแจ้ง')
      .setChoiceValues([
        '💡 เสนอแนะฟังก์ชันใหม่ / วิธีวิเคราะห์ที่อยากให้เพิ่ม',
        '🐞 รายงานข้อผิดพลาด (Bug / คำนวณไม่ผ่าน / กราฟผิดปกติ)',
        '🔢 ผลไม่ตรงกับ PBTools หรือโปรแกรมอื่น',
        '💬 ข้อเสนอแนะทั่วไป / ความสะดวกในการใช้งาน',
        '❓ สอบถามวิธีการใช้งาน / วิธีแปลผล'
      ])
      .setRequired(true);

  // 2. Screens
  form.addCheckboxItem()
      .setTitle('2. หน้าจอหรือฟังก์ชันที่เกี่ยวข้อง (เลือกได้มากกว่า 1 ข้อ)')
      .setChoiceValues([
        'หน้าแรก (Home)',
        'Single environment (วิเคราะห์ทีละสถานที่)',
        'Multi-environment (วิเคราะห์รวมหลายสถานที่: Combined ANOVA, one-stage, two-stage)',
        'GxE & stability (Eberhart-Russell, Shukla, AMMI, GGE biplot, WAASB, MTSI, MGIDI)',
        'การเปรียบเทียบค่าเฉลี่ย (LSD, DMRT, Tukey, Dunnett)',
        'การนำเข้าไฟล์ Excel / CSV',
        'กราฟและการบันทึกรูปภาพ (Plot / Live View)',
        'การดาวน์โหลดรายงาน (Excel / Word)',
        'หน้า Methods / About',
        'อื่นๆ'
      ])
      .setRequired(true);

  // 3. Details
  form.addParagraphTextItem()
      .setTitle('3. รายละเอียดข้อเสนอแนะ หรือปัญหาที่พบ')
      .setHelpText('ระบุขั้นตอนที่ทำแล้วเกิดปัญหา ข้อความที่แสดง หรือลักษณะฟังก์ชันที่อยากได้ ' +
                   'ถ้าผลไม่ตรงกับ PBTools ให้ระบุค่าที่ได้จากทั้งสองโปรแกรม')
      .setRequired(true);

  // 4. Diagnostic info (optional)
  form.addParagraphTextItem()
      .setTitle('4. ข้อมูลวินิจฉัยระบบ (Diagnostic Info)')
      .setHelpText('ถ้าพบปัญหา กดปุ่ม "คัดลอกข้อมูลวินิจฉัย" ใน RBTool แล้ววางข้อความในช่องนี้ ' +
                   '(มีรุ่นของแอป ค่าที่เลือกในหน้าจอ และข้อความ error ไม่มีค่าข้อมูลดิบ)');

  // 5. Contact (optional)
  form.addTextItem()
      .setTitle('5. ชื่อผู้แจ้ง / หน่วยงาน / อีเมลหรือเบอร์โทรติดต่อกลับ (ไม่บังคับ)')
      .setHelpText('เพื่อสอบถามข้อมูลเพิ่มเติม หรือแจ้งเมื่อแก้ไขปัญหาเรียบร้อย');

  // Response sheet
  var sheet = SpreadsheetApp.create('RBTool - บันทึกข้อเสนอแนะและแจ้งปัญหา (Responses)');
  form.setDestination(FormApp.DestinationType.SPREADSHEET, sheet.getId());

  Logger.log('======================================================================');
  Logger.log('[1] Public form link (give this to the maintainer): ' + form.getPublishedUrl());
  Logger.log('[2] Edit form: ' + form.getEditUrl());
  Logger.log('[3] Responses sheet: ' + sheet.getUrl());
  Logger.log('======================================================================');
}
